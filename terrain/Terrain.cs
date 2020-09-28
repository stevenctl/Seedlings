using System;
using System.Collections.Generic;
using System.Linq;
using Godot;

enum EdgeType {
	InnerCorner,
	OuterCorner,
	Edge,
}

struct Edge {
	public Vector2[] Polygon;
	public EdgeType Type;
}

[Tool]
public class Terrain : Polygon2D {
	[Export] public Texture edge;

	public override void _Ready() {
		Color = Colors.Transparent;
	}

	private Curve2D ToCurve() {
		var curve = new Curve2D();
		foreach (var p in Polygon) {
			curve.AddPoint(p);
			// TODO logic for adding automatic control points to make things round
		}

		curve.AddPoint(Polygon[0]);
		curve.AddPoint(Polygon[1]);

		return curve;
	}

	private static readonly Color[] colArray = {
		Colors.White, Colors.White, Colors.White, Colors.White,
	};

	private static readonly Color[] colArray3 = {
		Colors.White, Colors.White, Colors.White
	};

	public override void _Draw() {
		// if we don't have the edge 
		if (edge == null) {
			return;
		}

		// convert to a curve which will apply the "curve mode"
		var curve = ToCurve();

		// generate normals from baked curve
		var normals = new List<Vector2>();
		for (var i = 0; i < curve.GetBakedPoints().Length; i++) {
			var cur = i >= curve.GetBakedPoints().Length ? 0 : i;

			var startIdx = cur - 1;
			if (startIdx < 0) {
				startIdx = curve.GetBakedPoints().Length + startIdx;
			}

			var endIdx = cur + 1;
			if (endIdx > curve.GetBakedPoints().Length - 1) {
				endIdx = endIdx - curve.GetBakedPoints().Length;
			}

			var prev = curve.GetBakedPoints()[startIdx];
			var mid = curve.GetBakedPoints()[cur];
			var next = curve.GetBakedPoints()[endIdx];

			// interpolation between this point and the next one
			var a = (prev - mid).Normalized();
			var b = (next - mid).Normalized();

			// interpolate between those points to get the final normal
			var normal = new Vector2(
				-(b - a).Normalized().y,
				(b - a).Normalized().x
			) * edge.GetHeight() * -1;

			normals.Add(normal);
		}


		var uvFraction = curve.BakeInterval / edge.GetWidth();
		var uvSegments = new Vector2[(int) (1 / uvFraction)][];
		var uvOffset = 0f;
		for (var i = 0; i < uvSegments.Length; i++) {
			uvSegments[i] = new[] {
				new Vector2(uvOffset, 0),
				new Vector2(uvOffset + uvFraction, 0),
				new Vector2(uvOffset + uvFraction, 1),
				new Vector2(uvOffset, 1),
			};
			uvOffset += uvFraction;
		}


		var edges = new Edge[normals.Count];
		// draw "sub" polygons from each normal, with the texture mapped to each of these polygons
		for (var i = 0; i < normals.Count; i++) {
			var curPt = i < curve.GetBakedPoints().Length ? i : 0;
			var next = i + 1 < normals.Count ? i + 1 : 0;
			var subPoly = new[] {
				curve.GetBakedPoints()[curPt] + normals[i],
				curve.GetBakedPoints()[next] + normals[next],
				curve.GetBakedPoints()[next] - normals[next],
				curve.GetBakedPoints()[curPt] - normals[i],
			};
			edges[i] = CalcEdge(subPoly);
			if (edges[i].Type == EdgeType.Edge) {
				DrawPolygon(edges[i].Polygon, colArray, uvSegments[i % uvSegments.Length], edge);
			}
		}

		foreach (var e in edges) {
			if (e.Type == EdgeType.Edge) continue;
			DrawPolyline(e.Polygon, e.Type == EdgeType.InnerCorner ? Colors.Aqua : Colors.Orange);
		}
	}

	private static Edge CalcEdge(Vector2[] srcPoly) {
		if (srcPoly.Length != 4) {
			throw new ArgumentException("The given polygon must have 4 vertices. Got: " + srcPoly.Length);
		}

		// Only segments 1 and 3 can have intersections - if that assumption breaks we can just loop with the
		// offset starting at 0 to check 0 and 2 as well. 
		for (var i = 0; i < 2; i++) {
			var aFrom = srcPoly[1];
			var aTo = srcPoly[2];
			var bFrom = srcPoly[3];
			var bTo = srcPoly[0];
			var intersection = Geometry.SegmentIntersectsSegment2d(aFrom, aTo, bFrom, bTo);

			if (intersection is Vector2 intersect) {
				// use the larger sub-triangle
				var triA = new[] {bTo, aFrom, intersect};
				var triB = new[] {aTo, intersect, bFrom};
				var areaA = TriangleArea(triA);
				var areaB = TriangleArea(triB);

				return areaA > areaB
					? new Edge {Type = EdgeType.OuterCorner, Polygon = triA}
					: new Edge {Type = EdgeType.InnerCorner, Polygon = triB};
			}
		}

		return new Edge {Type = EdgeType.Edge, Polygon = srcPoly};
	}

	private static float TriangleArea(Vector2[] triangle) {
		if (triangle.Length != 3) {
			throw new ArgumentException("The given polygon must have 3 vertices. Got: " + triangle.Length);
		}

		var a = triangle[0];
		var b = triangle[1];
		var c = triangle[2];


		return Math.Abs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2f;
	}
}
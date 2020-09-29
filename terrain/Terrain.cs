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
	public Vector2[] OrigPolygon;
	public EdgeType Type;
}

[Tool]
public class Terrain : Polygon2D {
	[Export] public Texture edge;

	private Curve2D ToCurve() {
		var curve = new Curve2D();
		curve.BakeInterval = 10;
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


		DrawPolygon(curve.GetBakedPoints(), null, null, Texture);

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
				DrawPolygon(Quad(edges[i].Polygon), colArray, uvSegments[i % uvSegments.Length], edge);
			}
		}

		var j = 0;
		foreach (var e in edges) {
			Color c;
			switch (e.Type) {
				case EdgeType.InnerCorner:
					c = Colors.Orange;
					break;
				case EdgeType.OuterCorner:
					c = j % 2 == 0 ? Colors.Yellow : Colors.Orange;
					break;
				default:
					c = j % uvSegments.Length == 0 ? Colors.Lime : Colors.Aqua;
					break;
			}

			var closed = Close(e.Polygon);
			DrawPolyline(closed, c);
			DrawCircle(curve.GetBakedPoints()[j], 1, c);
			j++;
		}
	}

	private static Vector2[] Close(IReadOnlyList<Vector2> poly) {
		var closed = new Vector2[poly.Count + 1];
		for (var i = 0; i < poly.Count; i++) {
			closed[i] = poly[i];
		}

		closed[closed.Length - 1] = closed[0];

		return closed;
	}

	private static Vector2[] Quad(Vector2[] poly) {
		if (poly.Length == 4) {
			return poly;
		}
		var closed = new Vector2[poly.Length + 1];
		for (var i = 0; i < poly.Length; i++) {
			closed[i] = poly[i];
		}

		return closed;
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
				var triB = new[] {aTo, bFrom, intersect};
				var areaA = TriangleArea(triA);
				var areaB = TriangleArea(triB);

				return areaA > areaB
					? new Edge {Type = EdgeType.OuterCorner, Polygon = triA, OrigPolygon = srcPoly}
					: new Edge {Type = EdgeType.InnerCorner, Polygon = triB, OrigPolygon = srcPoly};
			}
		}

		return new Edge {Type = EdgeType.Edge, Polygon = srcPoly, OrigPolygon = srcPoly};
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
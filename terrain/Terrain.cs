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
	private static readonly Color[] ColArray = {
		Colors.White, Colors.White, Colors.White, Colors.White,
	};

	[Export] public Texture EdgeTexture;
	[Export] public bool DrawEdges;

	private Curve2D GenCurve() {
		// TODO assuming i'd just get a curve from smartshape somehow
		if (_curve == null) _curve = new Curve2D();
		_curve.ClearPoints();
		_curve.BakeInterval = 5; // TODO parameterize

		foreach (var p in Polygon) {
			_curve.AddPoint(p);
		}
		// Close the polygon
		_curve.AddPoint(Polygon[0]);

		return _curve;
	}

	private Curve2D _curve;
	private Vector2[][] _uvSegments;

	private static Vector2 Rotate90(Vector2 v) {
		return new Vector2(v.y, -v.x);
	}

	public override void _Draw() {
		// if we don't have the edge 
		if (EdgeTexture == null) {
			return;
		}

		// just so I can write this code against Curve2D to easily port to smart shape
		GenCurve();
		var points = _curve.GetBakedPoints();

		// generate uv segments
		GenerateUvSegments();


		for (var i = 0; i < points.Length + 1; i++) {
			var next = i + 1 >= points.Length ? 0 : i + 1;
			var unitNormal = Rotate90((points[next] - points[i])).Normalized();
			var normal = unitNormal * EdgeTexture.GetHeight() / 2;
			var c = i % _uvSegments.Length != 0 ? Colors.Aqua : Colors.Green;
			var pt = _curve.GetBakedPoints()[i];
			DrawLine(pt, pt + normal, c);
			DrawLine(pt, pt - normal, c);
			DrawCircle(pt, 1, Colors.Blue);
		}
	}

	private void GenerateUvSegments() {
		// TODO - only have to do this when bake interval or edge texture change
		var uvFraction = _curve.BakeInterval / EdgeTexture.GetWidth();
		_uvSegments = new Vector2[(int) (1 / uvFraction)][];
		var uvOffset = 0f;
		for (var i = 0; i < _uvSegments.Length; i++) {
			_uvSegments[i] = new[] {
				new Vector2(uvOffset, 0),
				new Vector2(uvOffset + uvFraction, 0),
				new Vector2(uvOffset + uvFraction, 1),
				new Vector2(uvOffset, 1),
			};
			uvOffset += uvFraction;
		}
	}


}
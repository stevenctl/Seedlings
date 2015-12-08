package com.sugarware.seedlings;


public class Shaders {
	
	//Water Textures

	
	

	
	
	
	
	public static String waterFragmentShader = "#ifdef GL_ES\n"
			+ "precision mediump float;\n"
			+ "#endif\n"
			+ "varying vec4 v_color;\n"
			+ "varying vec2 v_texCoords;\n"
			+ "uniform sampler2D u_texture;\n"
			+ "uniform sampler2D u_texture2;\n"
			+ "uniform float timedelta;\n"
			+ "uniform float ampdelta;\n"
			+ "void main()                                  \n"
			+ "{                                            \n"
			+ "  vec2 displacement = texture2D(u_texture2, v_texCoords/6.0).xy;\n" //
			+ "  float t=v_texCoords.y +displacement.y*0.7-0.15+  (sin(v_texCoords.x * 40.0  + sin(ampdelta) * 0.75) * (0.115 * cos(ampdelta))); \n" //
			//+ "  float t=v_texCoords.y +displacement.y*0.7-0.15+  (sin(v_texCoords.x * 40.0+cos(ampdelta)*3 / 4) * (0.115 * cos(ampdelta))); \n" //
			+ "  gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x,t));\n"
			+ "}";
	
	public static String water2FragmentShader = "#ifdef GL_ES\n"
			+ "precision mediump float;\n"
			+ "#endif\n"
			+ "varying vec4 v_color;\n"
			+ "varying vec2 v_texCoords;\n"
			+ "uniform sampler2D u_texture;\n"
			+ "uniform sampler2D u_texture2;\n"
			+ "uniform float timedelta;\n"
			+ "uniform float ampdelta;\n"
			+ "void main()                                  \n"
			+ "{                                            \n"
			+ "  vec2 displacement = texture2D(u_texture2, v_texCoords/6.0).xy;\n" //
			+ "  float t= v_texCoords.y + displacement.y *0.1-0.15+ (sin (v_texCoords.x * 60.0+timedelta) * 0.005); \n" //
			//+ "  float t=v_texCoords.y +displacement.y*0.7-0.15+  (sin(v_texCoords.x * 40.0+cos(ampdelta)*3 / 4) * (0.115 * cos(ampdelta))); \n" //
			+ "  gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x,t));\n"
			+ "}";
	
	public static String vertexShader = 
			"attribute vec4 a_position;    \n"
			+ "attribute vec2 a_texCoord0;\n"
			+ "uniform mat4 u_projTrans;\n"
			+ "varying vec4 v_color;" 
			+ "varying vec2 v_texCoords;"
			+ "void main()                  \n"
			+ "{                            \n"
			+ "   v_color = vec4(1, 1, 1, 1); \n"
			+ "   v_texCoords = a_texCoord0; \n"
			+ "   gl_Position =  u_projTrans * a_position;  \n"
			+ "}";
	
	public static String ambientShader = 
					 "varying LOWP vec4 vColor; \n"
					+"varying vec2 vTexCoord; \n"
					+"uniform sampler2D u_texture; \n"
					+"uniform LOWP vec4 ambientColor; \n"
					+"void main() { \n"
					+"    vec4 diffuseColor = texture2D(u_texture, vTexCoord); \n"
					+"    vec3 ambient = ambientColor.rgb * ambientColor.a; \n"
					+"    vec3 final = vColor * diffuseColor.rgb * ambient; \n"
					+"    gl_FragColor = vec4(final, diffuseColor.a); \n"
					+"    "
					+"}";
	
	
	public static String lightShader = 
					 "varying LOWP vec4 vColor;\n"
					+"varying vec2 vTexCoord;\n"
					+"\n"
					+"//our texture samplers\n"
					+"uniform sampler2D u_texture; //diffuse map\n"
					+"uniform sampler2D u_lightmap;   //light map\n"
					+"\n"
					+"//resolution of screen\n"
					+"uniform vec2 resolution; \n"
					+" \n"
					+"void main() {\n"
					+"    vec2 lighCoord = (gl_FragCoord.xy / resolution.xy);\n"
					+"    vec4 Light = texture2D(u_lightmap, lighCoord);\n"
					+" \n"
					+"    gl_FragColor = vColor * Light;\n"
					+"}\n";
}

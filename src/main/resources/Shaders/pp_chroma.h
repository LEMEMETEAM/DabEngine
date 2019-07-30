vec3 chroma(sampler2D s, vec2 uv){
    vec4 r = vec4(0.0);
	vec4 g = vec4(0.0);
	vec4 b = vec4(0.0);

	r = texture(s, uv - 0.002);
	g = texture(s, uv - 0.005);
	b = texture(s, uv - 0.003);

	return vec3(r.r, g.g, b.b);
}
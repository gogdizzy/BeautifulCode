/*
@author gogdizzy
@date   2015-08-21
@desciption 收集到的一些概率方面代码片段
*/

// 从长度为N的数组中随机选出K个数
// 假设rand01()返回[0.0,1.0)的浮点数
void choose( int* in, int N, int* out, int K ) {
	for( int i = 0; i < K; ++i ) {
		int cur = int( rand01() * N );
		out[i] = in[cur];
		in[cur] = in[--N];
	}
}

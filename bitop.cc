/*
@author gogdizzy
@date   2015-08-20
@desciption 收集到的一些位运算代码片段
@resource
	http://graphics.stanford.edu/~seander/bithacks.html
*/

// 返回大于等于x的最小的2次幂，0返回0，负数返回0
// 根据这个方法，可以很容易写出无符号版本和64bit版本
int32_t nextpow2( int32_t x ) {
	--x;
	x |= ( x >> 1 );
	x |= ( x >> 2 );
	x |= ( x >> 4 );
	x |= ( x >> 8 );
	x |= ( x >> 16 );
	return ++x;
}

// 解析出最低bit
int32_t lowbit( int32_t x ) {
	return x & -x;
}

// 求绝对值
int32_t abs( int32_t x ) {
	return ( x ^ ( x >> 31 ) ) - ( x >> 31 );
}

float abs( float f ) {
	int x = *(int*)&f;
	x &= 0x7fffffff;
	return *(float*)&x;
}

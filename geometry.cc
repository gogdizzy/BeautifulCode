/*
@author gogdizzy
@date   2015-08-20
@desciption 收集到的一些几何代码片段
*/

// 求三角形内心
typdef std::complex< double > point;
point incenter( point a, point b, point c ) {
	double a = std::abs( p2 - p3 );
	double b = std::abs( p3 - p1 );
	double c = std::abs( p1 - p2 );
	return ( a * p1 + b * p2 + c * p3 ) / ( a + b + c );
}

/* 不使用+、-计算两个整数a、b的和
 *
 */

#include <stdio.h>

int add( int a, int b ) {
    return b ? add( a ^ b, ( a & b ) << 1 ) : a;
}

int add2( int a, int b ) {
    do {
        int tmp = a ^ b;
        a = ( a & b ) << 1;
        b = tmp;
    } while( a );
    return b;
}

int main() {
    printf( "%d + %d = %d\n", 1, 2, add( 1, 2 ) );
    printf( "%d + %d = %d\n", -1, 2, add( -1, 2 ) );
    printf( "%d + %d = %d\n", 4, 4, add( 4, 4 ) );
    printf( "%d + %d = %d\n", -2147483648, 2147483647, add( -2147483648, 2147483647 ) );
    printf( "%d + %d = %d\n", -2147483648, -2147483648, add( -2147483648, -2147483648 ) );
    printf( "%d + %d = %d\n", 2147483647, 2147483647, add( 2147483647, 2147483647 ) );
    return 0;
}

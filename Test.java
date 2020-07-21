/*
@author gogdizzy
@date   2020-07-21
@desciption 计算一种纸牌玩法
*/

import java.util.List;
import java.util.ArrayList;

public class Test {
    
    public static final int DIAMOND = 0;
    public static final int CLUB = 1;
    public static final int HEART = 2;
    public static final int SPADE = 3;

    public static class Card {
        public int suit;
        public int num;

        public Card( int suit, int num ) {
            this.suit = suit;
            this.num = num;
        }
    }

    public static class Segment {
        public int left;
        public int right;

        public Segment( int left, int right ) {
            this.left = left;
            this.right = right;
        }
    }

    private int replaceUsed;

    public int countBit1( int x ) {
        int ans = 0;
        while( x != 0 ) {
            x = ( x & ( x - 1 ) );
            ++ans;
        }
        return ans;
    }

    public boolean exist( int collections, int x ) {
        return ( ( 1 << x ) & collections ) != 0;
    }

    public int addElement( int collections, int x ) {
        return collections | ( 1 << x );
    }

    public List<Card> sortCards( List<Card> handCards ) {
        List<Card> sortedHandCards = new ArrayList<>(handCards);
        sortedHandCards.sort( (a, b) -> {
            if( a.suit < b.suit || a.suit == b.suit && a.num < b.num ) return -1;
            return 1;
        } );
        return sortedHandCards;
    }

    public boolean pureSeqOK( List<Card> sortedHandCards, int chosenCards ) {

        final int handCardCount = sortedHandCards.size();

        if( handCardCount <= 0 ) return false;

        List< List<Card> > pureSeqs = new ArrayList<>();

        for( int i = 0; i < handCardCount; ++i ) {
            if( exist( chosenCards, i ) ) {
                Card card = sortedHandCards.get( i );
                boolean appendOK = false;
                for( int k = pureSeqs.size() - 1; k >= 0; --k ) {
                    List<Card> pureSeq = pureSeqs.get( k );
                    Card lastCard = pureSeq.get( pureSeq.size() - 1 );
                    if( card.suit == lastCard.suit && card.num == lastCard.num + 1 ) {
                        pureSeq.add( card );
                        appendOK = true;
                        break;
                    }
                }
                if( !appendOK ) {
                    List<Card> newPureSeq = new ArrayList<>();
                    newPureSeq.add( card );
                    pureSeqs.add( newPureSeq );
                }
            }
        }

        for( int k = 0; k < pureSeqs.size(); ++k ) {
            if( pureSeqs.get( k ).size() < 3 ) return false;
        }

        return true;
    }

    public boolean commonSeqOK( List<Card> sortedHandCards, int chosenCards, int replace ) {

        final int handCardCount = sortedHandCards.size();

        if( handCardCount <= 0 ) return false;

        List< List<Card> > commonSeqs = new ArrayList<>();

        for( int i = 0; i < handCardCount; ++i ) {
            if( exist( chosenCards, i ) ) {
                Card card = sortedHandCards.get( i );
                boolean appendOK = false;
                for( int k = commonSeqs.size() - 1; k >= 0; --k ) {
                    List<Card> commonSeq = commonSeqs.get( k );
                    Card lastCard = commonSeq.get( commonSeq.size() - 1 );
                    if( card.suit == lastCard.suit && card.num > lastCard.num ) {
                        commonSeq.add( card );
                        appendOK = true;
                        break;
                    }
                }
                if( !appendOK ) {
                    List<Card> newCommonSeq = new ArrayList<>();
                    newCommonSeq.add( card );
                    commonSeqs.add( newCommonSeq );
                }
            }
        }

        replaceUsed = 0;
        for( int k = 0; k < commonSeqs.size(); ++k ) {
            List<Card> commonSeq = commonSeqs.get( k );
            List<Segment> segments = new ArrayList<>();
            int cardMask = 0;
            Card lastCard = commonSeq.get( 0 );
            cardMask = addElement( cardMask, lastCard.num );
            int left = lastCard.num, right = lastCard.num;
            for( int n = 1; n < commonSeq.size(); ++n ) {
                Card card = commonSeq.get( n );
                if( card.num != lastCard.num + 1 ) {
                    if( right - left <= 1 ) {
                        segments.add( new Segment( left, right ) );
                    }
                    left = card.num;
                }
                lastCard = card;
                right = card.num;
                cardMask = addElement( cardMask, lastCard.num );
            }
            if( right - left <= 1 ) {
                segments.add( new Segment( left, right ) );
            }

            final int segmentCount = segments.size();
            int minUsed = Integer.MAX_VALUE;
            for( int dirs = 0; dirs < ( 1 << segmentCount ); ++dirs ) {
                int replaceMask = 0;
                boolean replaceOK = true;
                for( int x = 0; x < segmentCount && replaceOK; ++x ) {
                    Segment segment = segments.get( x );
                    if( exist( dirs, x ) ) {
                        for( int z = segment.left - 1; z >= segment.right - 2; --z ) {
                            if( z < 0 ) {
                                replaceOK = false;
                                break;
                            }
                            replaceMask = addElement( replaceMask, z );
                        }
                    }
                    else {
                        for( int z = segment.right + 1; z <= segment.left + 2; ++z ) {
                            if( z > 13 ) {
                                replaceOK = false;
                                break;
                            }
                            replaceMask = addElement( replaceMask, z );
                        }
                    }
                }
                if( replaceOK ) {
                    replaceMask &= ( ~cardMask );
                    minUsed = Integer.min( minUsed, countBit1( replaceMask ) );
                }
            }

            replaceUsed += minUsed;
            if( replaceUsed > replace ) return false;
        }

        return replaceUsed <= replace;
    }

    public int removeSet( List<Card> sortedHandCards, int chosenCards, int replace ) {
        final int handCardCount = sortedHandCards.size();
        int[] count = new int[14];
        for( int i = 0; i < handCardCount; ++i ) {
            if( exist( chosenCards, i ) ) {
                Card card = sortedHandCards.get( i );
                ++count[card.num];
            }
        }

        int ans = 0;
        List<Integer> single = new ArrayList<>();
        List<Integer> pair = new ArrayList<>();
        for( int n = 13; n > 0; --n ) {
            if( count[n] == 1 ) {
                single.add( n );
                ans += n;
            }
            else if( count[n] == 2 ) {
                pair.add( n );
                ans += n * 2;
            }
        }

        for( int x = 0; x <= single.size() && x <= replace / 2; ++x ) {
            int tmp = 0;
            for( int reservedSingle = x; reservedSingle < single.size(); ++reservedSingle ) {
                tmp += single.get( reservedSingle );
            }
            for( int reservedPair = replace - x * 2; reservedPair < pair.size(); ++reservedPair ) {
                tmp += pair.get( reservedPair ) * 2;
            }
            ans = Integer.min( ans, tmp );
        }

        return ans;
    }

    public int calc( List<Card> handCards, int replace ) {
        int ans = Integer.MAX_VALUE;
        final int handCardCount = handCards.size();
        final int allCards = (1 << handCardCount) - 1;

        if( handCardCount <= 0 ) return 0;

        List<Card> sortedHandCards = sortCards( handCards );

        for( int pureSeqCards = 1; pureSeqCards <= allCards; ++pureSeqCards ) {
            if( !pureSeqOK( sortedHandCards, pureSeqCards ) ) continue;
            for( int commonSeqCards = 1; commonSeqCards <= allCards; ++commonSeqCards ) {
                if( ( commonSeqCards & pureSeqCards ) != 0 ) continue;
                if( !commonSeqOK( sortedHandCards, commonSeqCards, replace ) ) continue;
                int otherCards = ( allCards ^ ( pureSeqCards | commonSeqCards ) );
                int minPoint = removeSet( sortedHandCards, otherCards, replace - replaceUsed );
                ans = Integer.min( ans, minPoint );
            }
        }

        return ans;
    }

    public static void main( String[] args ) {
        Test t = new Test();

        List<Card> handCards = new ArrayList<>();
        handCards.add( new Card(SPADE, 1) );
        handCards.add( new Card(SPADE, 4) );
        handCards.add( new Card(SPADE, 11) );
        handCards.add( new Card(SPADE, 13) );
//        handCards.add( new Card(SPADE, 11) );
//        handCards.add( new Card(SPADE, 10) );

        List<Card> sortedHandCards = t.sortCards( handCards );

        System.out.println( t.removeSet( sortedHandCards, 0xFFFFFF, 4 ) );
        System.out.println( t.pureSeqOK( sortedHandCards, 0xFFFFFF ) );
        System.out.println( t.commonSeqOK( sortedHandCards, 0xFFFFFF, 2 ) );
        System.out.println( t.calc( sortedHandCards, 3 ) );
    }
}


public enum HandStrength
{
    STRAIGHTFLUSH(8),
    QUADS(7),
    FULLHOUSE(6),
    FLUSH(5),
    STRAIGHT(4),
    TRIPS(3),
    TWOPAIR(2),
    ONEPAIR(1),
    HIGHCARD(0);


    private final int rank;
    HandStrength(int rank)
    {
        this.rank = rank;
    }
}
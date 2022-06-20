package model;

public enum Round
{
    PREFLOP(0),
    FLOP(1),
    TURN(2),
    RIVER(3);

    private final int round;
    Round(int s)
    {
        this.round = s;
    }
}
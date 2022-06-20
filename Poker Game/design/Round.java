public enum Round
{
    PREFLOP("preflop"),
    FLOP("flop"),
    TURN("turn"),
    RIVER("river");

    private final String round;
    Round(String s)
    {
        this.round = s;
    }
}
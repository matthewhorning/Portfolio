public enum Action
{
    FOLD("fold"),
    CHECK("check"),
    CALL("call"),
    BET("bet"),
    RAISE("raise");

    private final String action;
    Action(String s)
    {
        this.action = s;
    }
}
class Log {
    public Account to;
    public int value;

    public Log(Account to, int value) {
        this.to = to;
        this.value = value;
    }

    @Override
    public String toString() {
        return to + " : " + value;
    }
}

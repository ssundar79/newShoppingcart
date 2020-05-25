package com.shopping.logger;

public interface ILogger {

    public abstract void setFormatter(IFormatter iformatter);

    public abstract String getId();

    public abstract String getType();

    public abstract void debug(String s);

    public abstract void debug(String s, String s1);

    public abstract void debug(String s, Throwable throwable);

    public abstract void debug(String s, String s1, Throwable throwable);

    public abstract void error(String s, Throwable throwable);

    public abstract void error(String s);

    public abstract void error(String s, String s1);

    public abstract void error(String s, String s1, Throwable throwable);

    public abstract void fatal(String s);

    public abstract void fatal(String s, Throwable throwable);

    public abstract void fatal(String s, String s1);

    public abstract void fatal(String s, String s1, Throwable throwable);

    public abstract void info(String s);

    public abstract void info(String s, Throwable throwable);

    public abstract void info(String s, String s1);

    public abstract void info(String s, String s1, Throwable throwable);

    public abstract void warn(String s);

    public abstract void warn(String s, Throwable throwable);

    public abstract void warn(String s, String s1);

    public abstract void warn(String s, String s1, Throwable throwable);
}

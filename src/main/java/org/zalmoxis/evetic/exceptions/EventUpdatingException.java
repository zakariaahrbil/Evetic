package org.zalmoxis.evetic.exceptions;

public class EventUpdatingException
        extends EventException
{
    public EventUpdatingException(String message)
    {
        super(message);
    }

    public EventUpdatingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EventUpdatingException(Throwable cause)
    {
        super(cause);
    }

    public EventUpdatingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EventUpdatingException()
    {
    }
}

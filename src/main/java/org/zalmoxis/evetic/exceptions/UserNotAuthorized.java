package org.zalmoxis.evetic.exceptions;

public class UserNotAuthorized
        extends RuntimeException
{
    public UserNotAuthorized(String message)
    {
        super(message);
    }

    public UserNotAuthorized(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UserNotAuthorized(Throwable cause)
    {
        super(cause);
    }

    public UserNotAuthorized(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotAuthorized()
    {
    }
}

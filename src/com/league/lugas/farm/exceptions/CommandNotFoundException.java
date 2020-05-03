package com.league.lugas.farm.exceptions;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException() {
        super("Command Not Found");
    }
}

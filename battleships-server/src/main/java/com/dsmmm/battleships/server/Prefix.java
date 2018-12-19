package com.dsmmm.battleships.server;

/**
 * @TODO: DOCUMENT ME
 */
public enum Prefix {
    CHAT("/chat"),
    SHOOT("/shoot");

    private String key;

    Prefix(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

    public String cipher(String toCipher) {
        return key +" "+ toCipher;
    }

    public static String decipher(String toDecipher) {
       Prefix prefix = getType(toDecipher);
       return toDecipher.substring(prefix.toString().length()+1);
    }

    public static Prefix getType(String message){
        for (Prefix prefix:Prefix.values()){
            if(message.startsWith(prefix.toString())){
                return prefix;
            }
        }
        throw new MessageTypeException();
    }
}

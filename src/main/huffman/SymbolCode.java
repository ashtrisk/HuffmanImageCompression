package main.huffman;

import java.util.BitSet;

/**
 * Created by Vostro-Daily on 12/3/2017.
 */

public class SymbolCode {

    private int symbol;
    private BitSet code;

    public SymbolCode(int symbol, BitSet code) {
        this.symbol = symbol;
        this.code = code;
    }

    public int getSymbol() {
        return symbol;
    }

    public BitSet getCode() {
        return code;
    }

}

package tila1;

import java.util.List;

public class Grammar {
    ITokenType left;
    List<ITokenType> right;
    List<ITokenType> firstPlus;

    public Grammar(ITokenType left, List<ITokenType> right) {
        this.left = left;
        this.right = right;
    }


}

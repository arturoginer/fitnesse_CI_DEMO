package fitnesse.wikitext.parser;

import util.Maybe;
import java.util.List;

public class DefineRule extends Rule {
    @Override
    public Maybe<Symbol> parse(Scanner scanner) {
        List<Token> tokens = scanner.nextTokens(new SymbolType[] {SymbolType.Whitespace, SymbolType.Text, SymbolType.Whitespace});
        if (tokens.size() == 0) return Symbol.Nothing;

        String name = tokens.get(1).getContent();
        if (!ScanString.isWord(name)) return Symbol.Nothing;

        scanner.moveNext();
        SymbolType open = scanner.getCurrent().getType();
        SymbolType close = SymbolType.closeType(open);
        if (close == SymbolType.Empty) return Symbol.Nothing;

        int start = scanner.getOffset();
        scanner.markStart();
        Symbol value = Parser.makeIgnoreFirst(getPage(), scanner, close).parse();
        if (scanner.isEnd()) return Symbol.Nothing;

        return new Maybe<Symbol>(new Symbol(SymbolType.Define)
                .add(name)
                .add(value)
                .add(scanner.substring(start, scanner.getOffset() - 1)));
    }
}

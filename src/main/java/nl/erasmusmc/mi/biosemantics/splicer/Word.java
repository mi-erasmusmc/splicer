package nl.erasmusmc.mi.biosemantics.splicer;

public class Word {

    String word;
    Method method;

    public Word(String word, Method method) {
        this.word = word;
        this.method = method;
    }

    public Word(String word) {
        this.word = word;
        this.method = Method.NONE;
    }

    @Override
    public String toString() {
        return word;
    }
}

package com.example.user.facedetectwithhellosystem.view.choose_lexicon.choose_lexicon_word;

public class Word {
    int id;
    String word;

    public Word(int id, String word) {
        this.id = id;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}

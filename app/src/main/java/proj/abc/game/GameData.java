package proj.abc.game;

import java.util.ArrayList;

/**
 * Created by mbarcelona on 4/9/16.
 */
public class GameData {

  int question;
  ArrayList<Integer> choices;
  int correctChoice;


  public GameData(int question, ArrayList<Integer> choices, int correctChoice){
    this.question = question;
    this.choices = choices;
    this.correctChoice = correctChoice;

  }

  public int getQuestion() {
    return question;
  }

  public void setQuestion(int question) {
    this.question = question;
  }

  public ArrayList<Integer> getChoices() {
    return choices;
  }

  public void setChoices(ArrayList<Integer> choices) {
    this.choices = choices;
  }

  public int getCorrectChoice() {
    return correctChoice;
  }

  public void setCorrectChoice(int correctChoice) {
    this.correctChoice = correctChoice;
  }
}

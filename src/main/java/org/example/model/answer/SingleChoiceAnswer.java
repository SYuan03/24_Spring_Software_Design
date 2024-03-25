package org.example.model.answer;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class SingleChoiceAnswer extends Answer {
    private int selectedOption;

    public SingleChoiceAnswer(int id, int selectedOption) {
        super(id);
        this.selectedOption = selectedOption;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}

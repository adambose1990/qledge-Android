/**
 * 
 */
package com.adam.qledge.data;

/**
 * @author Arindam
 *
 */
public class QuestionData {
	private String QuestionOID;
	private int category;
	private int subCategory;
	private String question;
	private String Answer1;
	private String Answer2;
	private String Answer3;
	private String Answer4;
	private int level;
	/**
	 * @return the questionOID
	 */
	public String getQuestionOID() {
		return QuestionOID;
	}
	/**
	 * @param questionOID the questionOID to set
	 */
	public void setQuestionOID(String questionOID) {
		QuestionOID = questionOID;
	}
	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(int category) {
		this.category = category;
	}
	/**
	 * @return the subCategory
	 */
	public int getSubCategory() {
		return subCategory;
	}
	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(int subCategory) {
		this.subCategory = subCategory;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the answer1
	 */
	public String getAnswer1() {
		return Answer1;
	}
	/**
	 * @param answer1 the answer1 to set
	 */
	public void setAnswer1(String answer1) {
		Answer1 = answer1;
	}
	/**
	 * @return the answer2
	 */
	public String getAnswer2() {
		return Answer2;
	}
	/**
	 * @param answer2 the answer2 to set
	 */
	public void setAnswer2(String answer2) {
		Answer2 = answer2;
	}
	/**
	 * @return the answer3
	 */
	public String getAnswer3() {
		return Answer3;
	}
	/**
	 * @param answer3 the answer3 to set
	 */
	public void setAnswer3(String answer3) {
		Answer3 = answer3;
	}
	/**
	 * @return the answer4
	 */
	public String getAnswer4() {
		return Answer4;
	}
	/**
	 * @param answer4 the answer4 to set
	 */
	public void setAnswer4(String answer4) {
		Answer4 = answer4;
	}
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
}

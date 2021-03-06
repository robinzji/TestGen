package maincode;
//	  TestGen: A tool for automatically generating intelligent test questions
//	  Author(s): Will Fehrnstrom, Robin Ji, Athan Chan, Tyler Abramson, Aiden O'neil
//	  Copyright (C) 2016 
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
public class Question {
	String rawText;
	String question;
	String[] answers;
	String codeBody;
	QuestionType type;
	int number;
	//Two possible types that a question can be: mutable, or reorderable.  
	//Mutable types will have their variable names and values changed.  
	//Reorderable types will just have their answers randomized, and 
	//their test number changed.  If it is a I, II, and III type of question, 
	//the method randomize will randomize what I, II, and III are too. 
	static final QuestionType[] reorderTypes = {QuestionType.CONDITIONAL, QuestionType.PURPOSE, QuestionType.MULTIPLEPOSS, QuestionType.CODEANSWER};
	static final QuestionType[] mutableTypes = {QuestionType.TRACE, QuestionType.RECURSION};
	//Array Structure is the following.  Reorder types, Mutable Types.
	//Within these, structure follows array structure of reorderTypes and mutableTypes.  The keywords are then 
	//in no particular order
	//QUESTION KEYWORDS ARE TO BE USED ON THE QUESTION SEGMENT ONLY
	static final KeywordBooleanTuple questionKeywords[][][] = {{{new KeywordBooleanTuple("What happens"), new KeywordBooleanTuple("which of the following can be used"), new KeywordBooleanTuple("does not compile"), new KeywordBooleanTuple("fixed"), new KeywordBooleanTuple("work as intended")},
		{new KeywordBooleanTuple("&&"), new KeywordBooleanTuple("||"), new KeywordBooleanTuple("!"), new KeywordBooleanTuple("true"), new KeywordBooleanTuple("false"), new KeywordBooleanTuple("conditional"), new KeywordBooleanTuple("and"), new KeywordBooleanTuple("or")}, 
		{new KeywordBooleanTuple("purpose")}, 
		{new KeywordBooleanTuple("replacement"), new KeywordBooleanTuple("is true")}, 
		{new KeywordBooleanTuple("replacement"), new KeywordBooleanTuple("correctly")}}, 
		//--------------------------------------------------------------
		{{new KeywordBooleanTuple("value"), new KeywordBooleanTuple("output"), new KeywordBooleanTuple("what is"), new KeywordBooleanTuple("printed")},
		{new KeywordBooleanTuple("trace"), new KeywordBooleanTuple("tracing")}, 
		{new KeywordBooleanTuple("recursion"), new KeywordBooleanTuple("recursive")}}};
	
	enum QuestionType {
		TRACE, CONDITIONAL, PURPOSE, RECURSION, MULTIPLEPOSS, CODEANSWER
	}
	
	public Question(String rawText){
		this.rawText = rawText;
		System.out.println(rawText);
	}

	public boolean isAMutableType(){
		for(int i = 0; i < mutableTypes.length; i++){
			if(type == mutableTypes[i]){
				return true;
			}
		}
		return false;
	}
	
	public Question generateNewQuestion(){
		return null;
	}
	
	public String generateQuestionText(){
		String questionText = "";
		return questionText;
	}
	
	public QuestionType determineType(){
		//Compute largest values in questionKeywords
		int largestAmountOfQuestions = 0;
		for(int i = 0; i < questionKeywords.length; i++){
			int count = questionKeywords[i].length;
			if(count > largestAmountOfQuestions){
				largestAmountOfQuestions = count;
			}
		}
		//Initialize an array with percentage similarities between the question and the questionType 
		double[][] percentageSimilarities = new double[questionKeywords.length][largestAmountOfQuestions - 1];
		//Outer loop controls biasing towards nonMutable methodology or mutable methodology for
		//randomizing the question
		for(int i = 0; i < questionKeywords.length; i++){
			int numOfGeneralTerms = questionKeywords[i][0].length;
			int numberOfGeneralMatches = 0;
			//Loop through the general terms that bias towards a certain
			//algorithm methodology
			for(int q = 0; q < questionKeywords[i][0].length; q++){
				if(question.toLowerCase().indexOf(questionKeywords[i][0][q].keyword) != -1){
					numberOfGeneralMatches++;
				}
			}
			//Iterate through the question types
			for(int j = 1; j < questionKeywords[i].length; j++){
				int totalQuestionTerms = numOfGeneralTerms;
				int questionTypeMatches = numberOfGeneralMatches;
				for(int k = 0; k < questionKeywords[i][j].length; k++){
					if(question.toLowerCase().indexOf(questionKeywords[i][j][k].keyword) != -1){
						questionTypeMatches++;
					}
					totalQuestionTerms++;
				}
				if(questionTypeMatches > totalQuestionTerms){
					System.out.println("Number of matches by approach algorithm exceeds number of cases.");
				}
				percentageSimilarities[i][j - 1] = ((float)questionTypeMatches)/totalQuestionTerms;
			}
		}
		//Finally, determine which questionType matches the question text
		//most closely
		double highestPercentage = 0f;
		int index = 0;
		int questionIndex = 0;
		for(int i = 0; i < percentageSimilarities.length; i++){
			for(int j = 0 ; j < percentageSimilarities[i].length; j++){
				if(percentageSimilarities[i][j] > highestPercentage){
					highestPercentage = percentageSimilarities[i][j];
					index = i;
					questionIndex = j;
				}
			}
		}
		if(index == 0){
			type = reorderTypes[questionIndex];
		}
		else if(index == 1){
			type = mutableTypes[questionIndex];
			System.out.println("Question match found.  Modifying question based on mutable approach......");
		}
		else{
			System.out.println("What the shit happened!?!?");
			return null;
		}
		System.out.println("Similarity of question to  question type: " + highestPercentage);
		return type;
	}
}

package testingsentimentanalysis;

// God damn netbeans can't find the libraries
// Somebody pls fix
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
// The import statement below has been updated with neural path for
// newest version of CoreNLP
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.LinkedList;
import java.util.Properties;
// Only use the below import if you feel like screwing with the probability matrix
// import org.ejml.simple.SimpleMatrix;

public class TestingSentimentAnalysis {

    public double oneValueNegative, twoValueNeuNegative, threeValueNeuPositive, fourValuePositive, totalSum;
    public String selectionName, averageRating;
    private StanfordCoreNLP pipeline;
    // Array of Strings/Reviews to be used later, especially in the analyzeData function

    public TestingSentimentAnalysis() {
        Properties extractedVariables = new Properties();
        //Telling it what information/processing I want to extract from the sentence
        extractedVariables.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(extractedVariables);    
    }

	// This is the heart of the program, it's going to rate the sentence with a score
    public int checkSentiment(String text) {        
        Annotation annotation = pipeline.process(text);
        
        LinkedList<Integer> object = new LinkedList<Integer>();
        
        int sentimentRanking = 0;
        
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            // Parse tree is getting the annotated tokens
            // "Sentiment" was added to SentimentAnnotatedTree            
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            // Using recurrent neural network to assign a sentiment ranking
            sentimentRanking = RNNCoreAnnotations.getPredictedClass(tree);
            //Summarizing findings
            System.out.println("GIVEN DATA: \"" + sentence.toString() + "\" SENTIMENT ANALYSIS RANK: " + sentimentRanking);
            
            object.add(sentimentRanking);
            // Feel free to screw around the code below, it should return the probability matrix
			
            //SimpleMatrix sm = RNNCoreAnnotations.getPredictedClass(tree);
        }    
        int sum = 0;
        for(int i = 0; i < object.size(); i++){
            sum += object.get(i);
            System.out.println(sum);
        }
        sum = sum/object.size();
        
        return sum;
    }
        public double[] valueCombination = {oneValueNegative, twoValueNeuNegative, threeValueNeuPositive, fourValuePositive};
	//This is the most important function for you egie and possibly the only one you will need to use SQL on
	public void initializeVariables(String rowName){
            Database accessKey = new Database();
            selectionName = rowName;
            
            oneValueNegative = accessKey.queryOne(rowName);
            twoValueNeuNegative = accessKey.queryTwo(rowName);
            threeValueNeuPositive = accessKey.queryThree(rowName);
            fourValuePositive = accessKey.queryFour(rowName);
            valueCombination[0] = oneValueNegative;
            valueCombination[1] = twoValueNeuNegative;
            valueCombination[2] = threeValueNeuPositive;
            valueCombination[3] = fourValuePositive;
            totalSum = accessKey.queryTotalAccum(rowName);
            for (double i: valueCombination){
                    totalSum += i;
            }
	}
	
	//Gets the most frequently occuring rating
	public String getMode(){
		//Find biggest
                String StrMode = "";
                int count = 0;
                double dubMode = 0.0;
                String x = "";
                for (double i:valueCombination){
                    count += 1;
                    if (count == 1){
                        x = "1";
                    }
                    else if (count == 2){
                        x = "2";
                    }
                    else if (count == 3){
                        x = "3";
                    }
                    else if (count == 4){
                        x = "4";
                    }
                    
                    if (i > dubMode){
                        StrMode = x;
                    }
                    else if (i == dubMode && i > 0.0){
                        StrMode += ("& " + x);
                    }
                }
		//double mode = java.util.Collections.max(java.util.Arrays.asList(oneValueNegative, twoValueNeuNegative, threeValueNeuPositive, fourValuePositive));
		
		//Lots of formatting for decimal places
		String str = String.format("\nThe most common rating for %s is %s\n", selectionName, StrMode);
                return str;
	}
	
	//Gets percentages of ratings
	public String getPercentage(){
		String str = String.format("\nThe percentage rated for Negative is: %.2f", ((oneValueNegative / totalSum) * 100), "%");
		str += String.format("\nThe percentage rated for Neutral/Negative is: %.2f", ((twoValueNeuNegative / totalSum) * 100), "%");
		str += String.format("\nThe percentage rated for Neutral/Positive is: %.2f", ((threeValueNeuPositive / totalSum) * 100), "%");
		str += String.format("\nThe percentage rated for Positive is: %.2f", ((fourValuePositive / totalSum) * 100), "%\n");
                return str;
	}
	
	//Gets total amount of ratings
	public String getTotals(){
            String str = "";
            str += String.format("\nThe total amount of negative reviews was: %.0f", oneValueNegative);
            str += String.format("\nThe total amount of neutral/negative reviews was: %.0f", twoValueNeuNegative);
            str += String.format("\nThe total amount of neutral/positive reviews was: %.0f", threeValueNeuPositive);
            str += String.format("\nThe total amount of positive reviews was: %.0f", fourValuePositive);
            str += String.format("\nThe total amount of reviews was: %.0f\n", totalSum);
            return str;
	}
	
	//Gets average rating, numerical and the correlated word
	public String getAverage(){
                String str = "";
		double average = (oneValueNegative + twoValueNeuNegative * 2 + threeValueNeuPositive * 3 + fourValuePositive * 4) / totalSum;
		if(average > 3){
			averageRating = "Positive";
		} else if(average > 2){
			averageRating = "Neutral/Positive";
		} else if(average > 1){
			averageRating = "Neutral/Negative";
		} else {
			averageRating = "Negative";
		}
		str += (String.format("\nThe average numerical rating of %s is %.2f", selectionName, average));
		str += (String.format("\nThis translates to an average qualitative value of ", averageRating));
                return str;
        }
}
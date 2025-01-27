package com.example;
import java.io.File;
import java.util.List;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;

public class Main {
    static int neighborhood_size = 5;
    DataModel dataModel = null;
    LogLikelihoodSimilarity logLikelihoodSimilarity = null;
    UserNeighborhood neighborhood = null;
    UserBasedRecommender recommender = null;

    static String[] books = { "Pride and Prejudice","The Great Gatsby", "To Kill a Mockingbird", "Brave New World","Moby-Dick","One Hundred Years of Solitude",
    "Uncle Tom's Cabin" };

   static String user_name [] = {"isha","neha","krishna","krish","jay","jayesh","aman","amisha","amit","ankit","nisha"};


   public static void user_preference(DataModel model ,long user_id){
    try{
        PreferenceArray user_pref = model.getPreferencesFromUser(user_id);
        System.out.println("userid preference :"+user_id);
        for(int i=0;i<user_pref.length();i++){
            long itemId = user_pref.getItemID(i);
            float preferenceValue = user_pref.getValue(i);
            System.out.println(itemId + "\t" + preferenceValue);    
        }

    }catch(Exception e){
        System.out.println("Error in user preference");
    }
   }
    public static void main(String[] args) {
        // System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        String path = "E:\\recommendation_system\\Book1.csv";
        try {
            DataModel model = new FileDataModel(new File(path));

            user_preference(model, 2);
            user_preference(model, 4);
            LogLikelihoodSimilarity similarity = new LogLikelihoodSimilarity(model);

           
            System.out.println("Similarity between user1 and user5 is :" + similarity.userSimilarity(2, 4));

            UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborhood_size, similarity, model);
            UserBasedRecommender recommender = new GenericUserBasedRecommender(model,
            neighborhood, similarity);

            List<RecommendedItem> recomendation = recommender.recommend(1, 5);

            System.out.println("Recommendation for customer : "+ user_name[0]+ " are");
            System.out.println("================");
            System.out.println("Bookid\title\t\testemeted preference");

            for(RecommendedItem item: recomendation){
                int book_id = (int)item.getItemID();
                float estimated_preference = recommender.estimatePreference(1, book_id);
                System.out.println(book_id + " "+ books[book_id-1]+"\t"+ estimated_preference);
            System.out.println(item);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
} 
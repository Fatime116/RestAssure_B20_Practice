package day06_SerializationAndDeserialization;

public class a {

    public static void main(String[] args) {

        String str ="apple";
        String unique ="";//ale  = a 0  p 1

        for (int i = 0; i < str.length(); i++) {//a
            int count = 0 ;
            for (int j = 0; j < str.length(); j++) {//apple
                if (str.charAt(i) ==str.charAt(j)){
                    count++;//1
                }
            }
            if(count==1){
                unique +="" + str.charAt(i);//a
                 unique=  unique  +  unique.indexOf(unique.charAt(i)) + " ";
            }
        }

        //unique ="ale"

        System.out.println(unique);//ale




    }

}

public class Question{
    String question, op1, op2, op3, op4;
    int answer;
    
    // Getters
    public String getQuestion(){
        return question;
    }
    
    public String getOp1(){
        return op1;
    }
    
    public String getOp2(){
        return op2;
    }
    
    public String getOp3(){
        return op3;
    }
    
    public String getOp4(){
        return op4;
    }
    
    public int getAnswer(){
        return answer;
    }
    
    // Setters
    public void setQuestion(String q){
        question = q;
    }
    
    public void setOp1(String o){
        op1 = o;
    }
    
    public void setOp2(String o){
        op2 = o;
    }
    
    public void setOp3(String o){
        op3 = o;
    }
    
    public void setOp4(String o){
        op4 = o;
    }
    
    public void setAnswer(int a){
        answer = a;
    }
}
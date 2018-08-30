import java.util.*;
import java.io.*;
class Genesis{
    public static Node genesis=null;
    public static GeneratePrime generatePrime;
    public static int nodeNumber=0;
    public static void main(String[] args)throws IOException{
        generatePrime = new GeneratePrime();
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("---Genesis Node---");
        System.out.println("Enter the date hyphen(-) separated (dd-mm-yyyy)");
        do{
            String[] timestamp=br.readLine().split("-");
            if(!checkValidDate(timestamp)){
                System.out.println("Wrong Format Please try again!!!");
            }
            else{
                Time time=new Time(timestamp);
                System.out.println("Please enter the data for genesis Node");
                String data = br.readLine();
                nodeNumber++;
                String id = uniqueCode(nodeNumber);
                genesis = new Node();
                String encryptedData = encryptData(data,nodeNumber);
                genesis.setData(time,encryptedData,nodeNumber,id,null,null,genesis);
                System.out.println("Genesis Node added Successfully");
                break;
            }
        }while(true);
        do{
            System.out.println("Add child nodes");
            System.out.println("Enter the nodeNumber of the parent node");
            int parentId=Integer.parseInt(br.readLine());
            Node child = new Node();
            SearchParent sp = new SearchParent();
            Node parent = sp.searchParent(genesis,parentId,nodeNumber);
            if(parent == null) {
                System.out.println("Id doesn't exist!!! Please try again");
            }
            else{
                System.out.println("Enter the date hyphen(-) separated (dd-mm-yyyy)");
                do{
                    String[] timestamp=br.readLine().split("-");
                    if(!checkValidDate(timestamp)){
                        System.out.println("Wrong Format Please try again!!!");
                    }
                    else{
                        Time time=new Time(timestamp);
                        System.out.println("Please enter the data for this Node");
                        String data = br.readLine();
                        nodeNumber++;
                        String id = uniqueCode(nodeNumber);
                        child = new Node();
                        String encryptedData = encryptData(data,nodeNumber);
                        child.setData(time,encryptedData,nodeNumber,id,parent,null,genesis);
                        parent.setChild(child);
                        System.out.println("Node added Successfully");
                        break;
                    }
                }while(true);
                System.out.println("Want to enter more nodes?? Y or N");
                String more = br.readLine();
                if(more.charAt(0)=='Y'||more.charAt(0)=='y')
                    continue;
                break;
            }
        }while(true);
    }
    
    public static String decryptData(String data,int nodeNumber){
        String ans="";
        int hashValue = GeneratePrime.getPrime(nodeNumber);
        for(int i=0;i<data.length();i++){
            int a=(int)(data.charAt(i));
            int b=a-65;
            a=b*26+a;
            a=a/(hashValue*hashValue);
            char z=(char)a;
            ans+=String.valueOf(z);
        }
        return ans;
    }
    public static String encryptData(String data,int nodeNumber){
        String ans="";
        int hashValue = GeneratePrime.getPrime(nodeNumber);
        for(int i=0;i<data.length();i++){
            int a=(int)(data.charAt(i));
            a=a*hashValue*hashValue;
            a=a%26;
            a=a+65;
            char z=(char)a;
            ans+=String.valueOf(z);
        }
        return ans;
    }
    
    public static boolean checkValidDate(String[] timeStamp){
        if(timeStamp.length!=3)
            return false;
        int[] time=new int[3];
        int idx=0;
        for(String vTime:timeStamp){
            time[idx++] = Integer.parseInt(vTime);
        }
        if(time[0]<1||time[0]>31)
            return false;
        if(time[1]<1||time[1]>12)
            return false;
        if(time[2]<1990||time[2]>2018)
            return false;
        return true;
    }
    public static String uniqueCode(int number){
        String a="";
        while(number>0){
            if(number%2==1)
                a+="1";
            else
                a+="0";
            number>>=1;
        }
        return a;
    }
    public static boolean[] nodeStatus;
    public static int longestChainAns;
    public static int tempAns;
    public static void longestChainUtil(Node genesis,int i){
        nodeStatus[i]=true;
        Iterator itr=genesis.childReferenceNodeId.iterator();
        while(itr.hasNext()){
            Node temp =(Node) itr.next();
            if(!nodeStatus[temp.nodeNumber]){
                longestChainUtil(temp,temp.nodeNumber);
                tempAns++;
            }
        }
    }
    public int longestChain(Node genesis,int nodeNumber){
        nodeStatus = new nodeStatus[nodeNumber];
        longestChainAns=0;
        Arrays.fill(nodeStatus,false);
        for(int i=0;i<nodeNumber;i++){
            tempAns=0;
            if(!nodeStatus[i]){
                longestChainUtil(genesis,i);
                longestChainAns=Math.max(longestChainAns,tempAns);
            }
        }
        return longestChainAns;
    }
}

class SearchParent{
    //applied DFS for parent Search
    //assuming all nodes Least common node is genesis node. 
    public static int findFlag=0;
    public static boolean[] node;
    public static Node ans=null;
    public void searchParentUtil(Node genesis,int parentId,int i){
        if(i==parentId){
            ans=genesis;
            return;
        }
        node[i]=true;
        Iterator itr=genesis.childReferenceNodeId.iterator();
        while(itr.hasNext()&&ans==null){
            Node temp=(Node)itr.next();
            if(!node[temp.nodeNumber]&&ans==null){
                searchParentUtil(temp,parentId,temp.nodeNumber);
            }
        }
    }
    public Node searchParent(Node genesis,int parentId,int nodeNumber){
        node = new boolean[nodeNumber];
        Arrays.fill(node,false);
        if(parentId>nodeNumber)
        for(int i=0;i<nodeNumber;i++){
            if(!node[i])
            searchParentUtil(genesis,parentId,i);
        }
        return ans;
    }
}

class NumberOfNodes{
    public static int nodeNumber=0;
    void setNumber(){
        nodeNumber++;
    }
    int getNumber(){
        return nodeNumber;
    }
}


class GeneratePrime{
    static ArrayList<Integer> arr=new ArrayList<>();
    GeneratePrime(){
        int n=10000007; 
        boolean[] prime = new boolean[n];
        Arrays.fill(prime,true);
        for(int i=2;i*i<=n;i++){
            if(prime[i])
            for(int j=2*i;j<n;j+=i){
                prime[j]=false;
            }
        }
        prime[0]=prime[1]=false;
        for(int i=0;i<n;i++){
            if(prime[i])
                arr.add(i);
        }
    }
    public static int getPrime(int n){
        return arr.get(n);
    }
}

class Node{
    Time timestamp;
    String data;
    int nodeNumber;
    String nodeId;
    Node referenceNodeId;
    HashSet<Node> childReferenceNodeId;
    Node genesisReferenceId;
    Node(){
        timestamp=new Time();
        data="";
        nodeId="";
        nodeNumber=0;
        childReferenceNodeId=new HashSet<>();
        genesisReferenceId=null;
        referenceNodeId=null;
    }
    public void setData(Time t, String d,int number,String id,Node parent,Node child,Node genesis){
        timestamp=t;
        data=d;
        nodeNumber=number;
        nodeId=id;
        referenceNodeId=parent;
        childReferenceNodeId.add(child);
        genesisReferenceId=genesis;
    }
    public void setChild(Node child){
        childReferenceNodeId.add(child);
    }
}

class Time{
    int date,month,year;
    Time(){
        date=1;
        month=1;
        year=1990;
    }
    Time(String[] timeStamp){
        int[] time=new int[3];
        int idx=0;
        for(String vTime:timeStamp){
            time[idx++] = Integer.parseInt(vTime);
        }
        date=time[0];
        month=time[1];
        year=time[2];
    }
}

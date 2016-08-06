package knowledgestructureelements;

public class Edge {
	public double weight;
	public Competence from;
	public Competence to;
	
	public Edge(){}
	
	public Edge(Competence from, Competence to, double weight){
		this.from = from;
		this.to= to;
		this.weight = weight;
	}
}

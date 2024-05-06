package graph;

public class Vertex {

public final String label;

public Vertex (String l)
{	label = l;
}


public String toString()
{	return label;
}


public boolean equals (Object o)
{	Vertex other = (Vertex) o;
	return (label.equals(other.label));
}


//vertices are as distinct as their labels
public int hashCode()
{	return label.hashCode();
}

}

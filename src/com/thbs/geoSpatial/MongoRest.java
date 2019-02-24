package com.thbs.geoSpatial;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.types.BasicBSONList;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Path("/geoSearch")
public class MongoRest {

	MongoClient client = new MongoClient();
	MongoCollection<Document> collection = client.getDatabase("geospatialData").getCollection("restaurants");

	@GET
	@Path("/getData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getData() throws IOException, ParseException {

		JSONArray result = new JSONArray();

		MongoCursor<Document> cursor = collection.find().iterator();

		while (cursor.hasNext()) {
			Document doc = cursor.next();
			doc.remove("_id");
			result.add(doc);
		}
		return Response.ok().entity(result.toString()).build();
	}

	@GET
	@Path("/getLocationData/{lat}/{long}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationData(@PathParam("lat") String lati, @PathParam("long") String longi)
			throws IOException, ParseException {

		JSONArray result = new JSONArray();

		Document location = new Document();
		BasicBSONList centerSphereQuery = new BasicBSONList();
		BasicBSONList points = new BasicBSONList();
		points.add(Double.parseDouble(lati));
		points.add(Double.parseDouble(longi));
		centerSphereQuery.add(points);
		centerSphereQuery.add(5 / 3963.2);

		location.put("$geoWithin", new Document("$centerSphere", centerSphereQuery));

		Document query = new Document();
		query.put("location", location);

		MongoCursor<Document> cursor = collection.find(query).iterator();

		while (cursor.hasNext()) {
			Document doc = cursor.next();
			doc.remove("_id");
			result.add(doc);
		}
		// System.out.println(result);

		// System.out.println(rows);
		return Response.ok().entity(result.toString()).build();
	}
}
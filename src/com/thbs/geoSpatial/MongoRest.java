package com.thbs.geoSpatial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Path("/geoSearch")
public class MongoRest {

	MongoClient client = new MongoClient();
	MongoCollection<Document> collection = client.getDatabase("geospatialData").getCollection("hospitals");

	@GET
	@Path("/getData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getData() throws IOException, ParseException {

		JSONArray result = new JSONArray();

		MongoCursor<Document> cursor = collection.find().limit(1000).iterator();

		while (cursor.hasNext()) {
			Document doc = cursor.next();
			doc.remove("_id");
			result.add(doc);
		}
		return Response.ok().entity(result.toString()).build();
	}

	@GET
	@Path("/2d/near/{hospitalName}/{distance}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNear(@PathParam("hospitalName") String hospitalName,
			@PathParam("distance") String maxDistance) throws IOException, ParseException {

		ArrayList coordinates = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find(new Document("name", hospitalName)).iterator();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			Document loc = (Document) doc.get("location");
			coordinates = (ArrayList) loc.get("coordinates");
		}

		JSONArray result = new JSONArray();

		if (coordinates.size() > 0) {
			Document location = new Document();
			location.put("$near", coordinates);
			location.put("$maxDistance", Double.parseDouble(maxDistance));

			Document query = new Document();
			query.put("location.coordinates", location);

			MongoCursor<Document> newcursor = collection.find(query).iterator();

			while (newcursor.hasNext()) {
				Document doc = newcursor.next();
				doc.remove("_id");
				result.add(doc);
			}
		}

		return Response.ok().entity(result.toString()).build();
	}

	@GET
	@Path("/2d/nearsphere/{hospitalName}/{distance}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNearSphere(@PathParam("hospitalName") String hospitalName,
			@PathParam("distance") String maxDistance) throws IOException, ParseException {

		ArrayList coordinates = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find(new Document("name", hospitalName)).iterator();
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			Document loc = (Document) doc.get("location");
			coordinates = (ArrayList) loc.get("coordinates");
		}

		JSONArray result = new JSONArray();

		if (coordinates.size() > 0) {
			Document location = new Document();
			location.put("$nearSphere", coordinates);
			location.put("$maxDistance", Double.parseDouble(maxDistance));

			Document query = new Document();
			query.put("location.coordinates", location);

			MongoCursor<Document> newcursor = collection.find(query).iterator();

			while (newcursor.hasNext()) {
				Document doc = newcursor.next();
				doc.remove("_id");
				result.add(doc);
			}
		}

		return Response.ok().entity(result.toString()).build();
	}
}
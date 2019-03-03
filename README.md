# geospatialSearch

Clone this project.

Sample dataset is available in the directory "dataset". Import the json into MongoDB.

Note: Database-geospatialData, Collection-hospitals

Create geospatial index in mongodb using the following command, db.hospitals.createIndex({"location":"2d"})

Build the war file and try out the following links,

  http://<host>:<port>/geospatialSearch/nearsphere.html

  http://<host>:<port>/geospatialSearch/near.html

// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.io.*; 

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
        Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        ArrayList<String> comments = new ArrayList<>();

        for (Entity entity : results.asIterable()) {
        String comment = (String) entity.getProperty("text-input");
        comments.add(" " + comment);
        }
    
        String json = convertToJson(comments);
        response.setContentType("application/json;");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the input from the form and add it to the comments list
        String text = request.getParameter("text-input");

        // Store data
        long timestamp = System.currentTimeMillis();
        Entity taskEntity = new Entity("Task");
        taskEntity.setProperty("text-input", text);
        taskEntity.setProperty("timestamp", timestamp);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(taskEntity);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");
    }

    /**
     * Converts a ServerStats instance into a JSON string using the Gson library.
     */
    private String convertToJson(ArrayList arr) {
        Gson gson = new Gson();
        String json = gson.toJson(arr);
        return json;
    }
}

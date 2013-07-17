/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fabcoders.persistence;

import com.hp.hpl.jena.sparql.util.Context;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.UpdateRequest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import org.apache.jena.atlas.web.auth.HttpAuthenticator;
import org.apache.jena.riot.WebContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class UpdateProcessRemote extends com.hp.hpl.jena.sparql.modify.UpdateProcessRemote
{
    private static final Logger log = LoggerFactory.getLogger(UpdateProcessRemote.class);
    
    private String user = null, password = null ;

    public UpdateProcessRemote(UpdateRequest request, String endpointURI, Context context, HttpAuthenticator authenticator)
    {
	super(request, endpointURI, context, authenticator);
    }

    @Override
    public GraphStore getGraphStore()
    {
        return null ;
    }
static int counter =0;
    @Override
    public void execute()
    {
        System.out.println("Database update call "+ counter++);
	Client client = Client.create();
	WebResource wr = client.resource(getEndpoint());
	client.addFilter(new LoggingFilter(System.out));

	if (user != null && password != null)
	{
	    if (log.isDebugEnabled()) log.debug("Setting HTTP Basic auth for endpoint {} with username {}", getEndpoint(), user);
	    client.addFilter(new HTTPBasicAuthFilter(user, password));
	}

	String reqStr = getUpdateRequest().toString();

	if (log.isDebugEnabled()) log.debug("Sending SPARQL request {} to endpoint {}", reqStr, getEndpoint());
	ClientResponse response =
	wr.type(WebContent.contentTypeSPARQLUpdate).
	accept(WebContent.contentTypeResultsXML).
	post(ClientResponse.class, reqStr);
	
	if (log.isDebugEnabled()) log.debug("SPARQL endpoint response: {}", response);
    }

    public void setBasicAuthentication(String user, String password)
    {
        this.user = user ;
        this.password = password ;
    }
}

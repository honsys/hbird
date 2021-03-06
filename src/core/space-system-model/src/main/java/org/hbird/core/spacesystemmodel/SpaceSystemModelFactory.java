package org.hbird.core.spacesystemmodel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hbird.core.spacesystemmodel.exceptions.InvalidSpaceSystemDefinitionException;

@Path("ssmfactory")
public interface SpaceSystemModelFactory {

	@GET
	@Path("model")
	@Produces({ "application/json", "application/xml" })
	SpaceSystemModel createSpaceSystemModel() throws InvalidSpaceSystemDefinitionException;

}
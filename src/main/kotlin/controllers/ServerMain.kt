package controllers

import io.javalin.Javalin

fun main(args: Array<String>) {
  val app = Javalin.create().port(getHerokuAssignedPort()).enableStandardRequestLogging().start()
  val service = PacemakerRestService()
  configRoutes(app, service)
}

private fun getHerokuAssignedPort(): Int {
  val processBuilder = ProcessBuilder()
  return if (processBuilder.environment()["PORT"] != null) {
    Integer.parseInt(processBuilder.environment()["PORT"])
  } else 7000
}

fun configRoutes(app: Javalin, service: PacemakerRestService) {
  app.before { ctx -> "Request: " + println(ctx.body())  } // debug
  app.get("/users") { ctx -> service.listUsers(ctx) }
  app.post("/users") { ctx -> service.createUser(ctx) }
  app.delete("/users") { ctx -> service.deleteUsers(ctx) }
  app.get("/users/:id/activities") { ctx -> service.getActivities(ctx) }
	app.get("/users/:id/activities/:type") { ctx -> service.getActivitiesType(ctx) }
  app.get("/users/:id/activities/:activityId") { ctx -> service.getActivity(ctx) }
  app.post("/users/:id/activities") { ctx -> service.createActivity(ctx) }
  app.post("/users/:id/activities/:activityId/locations") { ctx -> service.createLocation(ctx) }	
  app.delete("/users/:id/activities") { ctx -> service.deleteActivites(ctx) }
	app.post("/users/:id/friends/:email") { ctx -> service.createFriend(ctx) }
	app.delete("/users/:id/friends/:email") { ctx -> service.deleteFriend(ctx) }
	app.get("/users/:id/friends/") { ctx -> service.getFriends(ctx) }
	app.get("/users/:id/friends/:email/activities") { ctx -> service.getFriendActivities(ctx) }
	app.post("/users/:id/messages/:email") { ctx -> service.sendMessage(ctx) }
	app.get("/users/:id/messages/") { ctx -> service.getMessages(ctx) }
	app.post("/users/:id/messages/") { ctx -> service.broadcastMessage(ctx) }	
	app.after { ctx -> println("Response: " + ctx.resultString())  } // debug
}
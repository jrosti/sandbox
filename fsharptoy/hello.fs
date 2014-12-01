module WebServers
 
open System
open System.IO
open Nancy
open Nancy.Hosting.Self
open Nancy.Conventions
open MongoDB.Driver
 
let mongo = new Mongo()
let connected = mongo.Connect()
 
let db = mongo.["test"]

type WebServerModule() as this =
  inherit NancyModule()
  do this.Get.["/"] <-
   fun _ -> "Hello" :> obj

let startAt host =
  let nancyHost = new NancyHost(new Uri(host))
  nancyHost.Start()
  nancyHost

let server = startAt "http://localhost:8080/"
printfn "Press [Enter] to exit."
Console.ReadKey() |> ignore
server.Stop()

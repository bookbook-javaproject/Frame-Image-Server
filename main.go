package main

import (
	"log"
	"os"

	"github.com/fasthttp/router"
	"github.com/valyala/fasthttp"

	"frame/db"
	"frame/handler"
)

func main() {
	address := os.Getenv("MYSQL_URL")
	dbCon, err := db.InitDB(address)
	if err != nil {
		log.Panic("Connect Failed")
	}

	r := router.New()
	r.GET("/", func(ctx *fasthttp.RequestCtx) {
		handler.GetImage(ctx, dbCon)
	})

	log.Fatal(fasthttp.ListenAndServe(":5005", r.Handler))
}
#!/bin/bash

curl -XPUT http://cluster1-master-node:9200/address -H Content-Type:application/json -d '
{
	"mappings" : {
		"properties" : {
			"poi_code" : {"type" : "keyword"},
			"fname" : {"type" : "text"},
			"cname" : {"type" : "text"},
			"phone_a" : {"type" : "integer"},
			"phone_b" : {"type" : "integer"},
			"phone_c" : {"type" : "integer"},
			"zip_code" : {"type" : "integer"},
			"address" : {"type" : "text"},
			"san_bun" : {"type" : "integer"},
			"primary_bun" : {"type" : "integer"},
			"secondary" : {"type" : "integer"},
			"logtitute" : {"type" : "geo_point"},
			"latitude" : {"type" : "geo_point"}
		}
	}
}'

sleep 2

curl -XPUT http://cluster1-master-node:9200/poi -H Content-Type:application/json -d '
{
	"mappings" : {
		"properties" : {
			"poi_code" : {"type" : "keyword"},
			"large_category" : {"type" : "keyword"},
			"medium_category" : {"type" : "keyword"},
			"small_category" : {"type" : "keyword"}
		}
	}
}'
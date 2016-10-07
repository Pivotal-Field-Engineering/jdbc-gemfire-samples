#!/bin/bash

args="-file=/usr/local/middleware/cron/sqlfire/deleteParrsAppts.sqlf"
args="$args -user=svc_mw_sqlfire_admin -password=FireLogin12390!"
args="$args -client-bind-address=TTGNAPP0VRHSF01.TTGTPMG.NET"
args="$args -client-port=1527"

echo "running delete parrs appointments, started on `date`"

/usr/local/middleware/cron/sqlfire/sqlf run $args

echo "parrs appointments daily delete done, completed on `date`"
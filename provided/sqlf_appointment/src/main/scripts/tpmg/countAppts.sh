#!/bin/bash

args="-file=/usr/local/middleware/cron/sqlfire/countAppts.sqlf"
args="$args -user=svc_mw_sqlfire_admin -password=FireLogin12390!"
args="$args -client-bind-address=TTGNAPR0VRHSF01.TTGTPMG.NET"
args="$args -client-port=1527"

echo "running count parrs appointments, started on `date`"

/usr/local/middleware/cron/sqlfire/sqlf run $args

echo "count parrs appointments done, completed on `date`"
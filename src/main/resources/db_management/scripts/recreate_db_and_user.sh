#!/bin/bash
sshpass -f ../password/pass mysql -u root -p < ../sql/recreate_db_and_user.sql

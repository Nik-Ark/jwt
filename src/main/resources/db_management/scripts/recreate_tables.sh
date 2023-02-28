#!/bin/bash
sshpass -f ../password/jwt_user_pass mysql -u jwt_user -p < ../sql/recreate_tables.sql
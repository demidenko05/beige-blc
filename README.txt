site: https://sites.google.com/site/beigesoftware

Beigesoft™ common business logic library.

It's based on beige-bcommon.
It consists of common business logic, e.g. persistable models to store languages, countries, users,
abstraction and implementations of services like ORM, JDBC, CSV writer, etc.

To compile you need PostgreSql and MySql databases bsblct with user/password beigeaccounting, see test package for details.

to fix MySQL error "The server time zone value 'MSK' is unrecognized or represents more than one time zone":
add into /etc/mysql/mariadb.conf.d/50-server.cnf:
default_time_zone='+03:00'

this doesn't work despite of tables are populated by this:
mysql_tzinfo_to_sql /usr/share/zoneinfo | mysql -u root -p mysql

debug range #5-8 (5000..8999)

licenses:
BSD 2-Clause License
https://sites.google.com/site/beigesoftware/bsd2csl

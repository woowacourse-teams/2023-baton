ALTER TABLE runner_post ADD COLUMN implemented_contents TEXT AFTER title;
ALTER TABLE runner_post ADD COLUMN curious_contents TEXT AFTER implemented_contents;
ALTER TABLE runner_post ADD COLUMN postscript_contents TEXT AFTER curious_contents;

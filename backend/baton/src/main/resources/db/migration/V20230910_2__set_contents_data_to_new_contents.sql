UPDATE runner_post SET implemented_contents = contents;
UPDATE runner_post SET curious_contents = '';
UPDATE runner_post SET postscript_contents = '';

ALTER TABLE runner_post MODIFY implemented_contents TEXT NOT NULL;
ALTER TABLE runner_post MODIFY curious_contents TEXT NOT NULL;
ALTER TABLE runner_post MODIFY postscript_contents TEXT NOT NULL;

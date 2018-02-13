import os

DIR = os.path.join("com.google.intelligence.sense", "res")

def dump_strings_file(filepath, line):
	file = open(filepath, "w")
	file.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
	file.write("<resources>\n")
	file.write(line)
	file.write("</resources>\n")

for folder in os.listdir(DIR):
	path = os.path.join(DIR, folder)
	if not os.path.isdir(path):
		continue

	for file in os.listdir(path):
		if file != "strings.xml":
			continue

		filepath = os.path.join(path, file)
		for line in open(filepath):
			if "song_format_string" in line:
				outdir = os.path.join("res", folder)
				os.makedirs(outdir, exist_ok=True)
				dump_strings_file(os.path.join(outdir, file), line)
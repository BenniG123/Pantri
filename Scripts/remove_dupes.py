
def main():
    with open("more_recipes_2.txt", "r") as infile:
        with open("recipes.txt", "w") as outfile:
            seen = set()
            for line in infile:
                line = line.strip()
                r_id = int(line.split('/')[2])
                
                if not r_id in seen:
                    seen.add(r_id)
                    outfile.write(line+'\n')

if __name__ == "__main__":
    main()
from nltk.corpus import wordnet
from nltk import word_tokenize
import sys



function_words = ['about', 'across', 'against', 'along', 'around', 'at',
                 'behind', 'beside', 'besides', 'by', 'despite', 'down',
                 'during', 'for', 'from', 'in', 'inside', 'into', 'near', 'of',
                 'off', 'on', 'onto', 'over', 'through', 'to', 'toward',
                 'with', 'within', 'without', 'anything', 'everything',
                 'anyone', 'everyone', 'ones', 'such', 'it', 'itself',
                 'something', 'nothing', 'someone', 'the', 'some', 'this',
                 'that', 'every', 'all', 'both', 'one', 'first', 'other',
                 'next', 'many', 'much', 'more', 'most', 'several', 'no', 'a',
                 'an', 'any', 'each', 'no', 'half', 'twice', 'two', 'second',
                 'another', 'last', 'few', 'little', 'less', 'least', 'own',
                 'and', 'but', 'after', 'when', 'as', 'because', 'if', 'what',
                 'where', 'which', 'how', 'than', 'or', 'so', 'before', 'since',
                 'while', 'although', 'though', 'who', 'whose', 'can', 'may',
                 'will', 'shall', 'could', 'be', 'do', 'have', 'might', 'would',
                 'should', 'must', 'here', 'there', 'now', 'then', 'always',
                 'never', 'sometimes', 'usually', 'often', 'therefore',
                 'however', 'besides', 'moreover', 'though', 'otherwise',
                 'else', 'instead', 'anyway', 'incidentally', 'meanwhile', 'is', 'the']

def overlapcontext(synset, sentence):
    gloss = set(word_tokenize(synset.definition()))
    for example in synset.examples():
        example_token = set(word_tokenize(example))
        gloss = gloss.union(example_token)
    gloss = gloss.difference(function_words)
    return len(gloss.intersection(sentence))

def lesk( word, sentence ):
    best_sense = None
    max_overlap = 0
    context_sentence = set(sentence.split())
    context_sentence = context_sentence.difference(function_words)
    for sense in wordnet.synsets(word):
        overlap = overlapcontext(sense, context_sentence)
        print(overlap, sense.name())
        if overlap > max_overlap:
                max_overlap = overlap
                best_sense = sense
    return best_sense


if __name__ == "__main__":

    if (len(sys.argv)) < 3:  # Print error if number of arguments is less than 3
        print("Invalid number of arguments")
    else:
        word = sys.argv[1]
        sentence = sys.argv[2]
        result = lesk(word, sentence.lower())
        print("The best sense is ", result.name())
        if result is not None:
            print("Meaning:", result.definition())


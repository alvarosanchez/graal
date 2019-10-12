/*
 * Copyright (c) 2018, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.truffle.regex.tregex.parser.ast.visitors;

import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.regex.tregex.parser.ast.BackReference;
import com.oracle.truffle.regex.tregex.parser.ast.CharacterClass;
import com.oracle.truffle.regex.tregex.parser.ast.Group;
import com.oracle.truffle.regex.tregex.parser.ast.LookAheadAssertion;
import com.oracle.truffle.regex.tregex.parser.ast.LookBehindAssertion;
import com.oracle.truffle.regex.tregex.parser.ast.MatchFound;
import com.oracle.truffle.regex.tregex.parser.ast.PositionAssertion;
import com.oracle.truffle.regex.tregex.parser.ast.Sequence;

/**
 * This visitor is used for setting the {@link SourceSection} of AST subtrees that are copied into
 * the parser tree as substitutions for things like word boundaries and position assertions in
 * multi-line mode. It will set the source section of all nodes in the subtree to the
 * {@link SourceSection} object passed to {@link #run(Group, SourceSection)}.
 *
 * @see com.oracle.truffle.regex.tregex.parser.RegexParser
 */
public final class SetSourceSectionVisitor extends DepthFirstTraversalRegexASTVisitor {

    private SourceSection sourceSection;

    public void run(Group root, SourceSection srcSection) {
        this.sourceSection = srcSection;
        run(root);
    }

    @Override
    protected void visit(BackReference backReference) {
        backReference.setSourceSection(sourceSection);
    }

    @Override
    protected void visit(Group group) {
        group.setSourceSectionBegin(null);
        group.setSourceSectionEnd(null);
        group.setSourceSection(sourceSection);
    }

    @Override
    protected void leave(Group group) {
    }

    @Override
    protected void visit(Sequence sequence) {
        sequence.setSourceSection(sourceSection);
    }

    @Override
    protected void visit(PositionAssertion assertion) {
        assertion.setSourceSection(sourceSection);
    }

    @Override
    protected void visit(LookBehindAssertion assertion) {
        // look-around assertions always delegate their source section information to their inner
        // group
    }

    @Override
    protected void visit(LookAheadAssertion assertion) {
        // look-around assertions always delegate their source section information to their inner
        // group
    }

    @Override
    protected void visit(CharacterClass characterClass) {
        characterClass.setSourceSection(sourceSection);
    }

    @Override
    protected void visit(MatchFound matchFound) {
        // match-found nodes delegate their source section information to their parent subtree root
        // node
    }
}
